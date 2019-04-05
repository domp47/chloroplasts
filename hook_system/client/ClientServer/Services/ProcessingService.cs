using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using System.Net.Http.Headers;

using ClientServer.Models;

/*
This deals with all of the communication between the Client Server and the Processing Server
 */

namespace ClientServer.Services
{
    public interface IProcessingService
    {
        Task<ResultsResponse> InitiateUpload(Package package);
        Task<ResultsResponse> RequestResults(string jobId);
    }

    public class ProcessingService : IProcessingService
    {
        private readonly IHttpClientFactory _clientFactory;
        private readonly IXMLService _xmlService;
        private readonly IScrubbingService _scrubbingService;
        private readonly string _institutionId;

        public ProcessingService(IHttpClientFactory clientFactory, IXMLService xmlService, IScrubbingService scrubbingService, IConfiguration configuration)
        {
            _clientFactory = clientFactory;
            _xmlService = xmlService;
            _scrubbingService = scrubbingService;
            _institutionId = configuration.GetSection("ProcessingConfigurations")["InstitutionId"];
        }

        public async Task<ResultsResponse> InitiateUpload(Package package)
        {
            var filename = _scrubbingService.ScrubPackage(package);

            var uploadRequest = CreateUploadRequest(filename);

            var client = _clientFactory.CreateClient("processing");

            var requestAddress = $"api/submit?userId={uploadRequest.InstitutionId}&email={uploadRequest.Email}";

            // Create the multipart form portion of the request
            var formDataContent = new MultipartFormDataContent();

            // Create a form part for the file
            var fileContent = new StreamContent(File.OpenRead(filename))
            {
                Headers = 
                {
                    ContentLength = new FileInfo(filename).Length,
                    ContentType = new MediaTypeHeaderValue("application/zip")
                }
            };

            // Add the file form part to our form data
            formDataContent.Add(fileContent, "data", uploadRequest.FileName);

            // Send to the processing server
            var response = await client.PostAsync(requestAddress, formDataContent);
            var responseText = await response.Content.ReadAsStringAsync();

            var resultsResponse = JsonConvert.DeserializeObject<ResultsResponse>(responseText);
            
            return resultsResponse;
        }

        public UploadRequest CreateUploadRequest(string filename) 
        {
            // TODO: Get real data
            return new UploadRequest { 
                InstitutionId = _institutionId, 
                Email = "jb15iq@brocku.ca", // TODO: Should come from auth service
                FileName = filename
            };
        }

        public async Task<ResultsResponse> RequestResults(string jobId)
        {
            var resultsRequest = new ResultsRequest {
                InstitutionId = _institutionId, 
                JobId = jobId
            }; 

            var client = _clientFactory.CreateClient("processing");

            // Send to processing server
            var content = new StringContent(JsonConvert.SerializeObject(resultsRequest), Encoding.UTF8, "application/json");
            var requestAddress = $"api/results?userId={resultsRequest.InstitutionId}&jobId={resultsRequest.JobId}";
            var response = await client.PostAsync(requestAddress, content);

            // Handle Response
            var responseText = await response.Content.ReadAsStringAsync();
            var resultsResponse = JsonConvert.DeserializeObject<ResultsResponse>(responseText);

            var result = await _xmlService.ParseXMLFile(resultsResponse.Results);
            resultsResponse.Result = result;
            return resultsResponse;
        }
    }

    public class UploadRequest
    {
        public string InstitutionId { get; set; }
        public string Email { get; set; }
        public string FileName { get; set; } 
    }

    public class ResultsRequest
    {
        public string InstitutionId { get; set; }
        public string JobId { get; set; }
    }

    public class ResultsResponse
    {
        public string Status { get; set; }
        public string JobId { get; set; }
        public DateTime EstimatedCompletion { get; set; }
        public string Results { get; set; } 

        public Result Result { get; set; }
    }
}