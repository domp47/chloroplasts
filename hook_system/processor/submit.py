import tarfile as tar

def submission(userID: str, email: str, data) -> str:
    
    # Discus whether we want to save it to a temp file and then send the path or just use the library thats built in to use memory

    with tar.open(fileobj=data.stream, mode='r') as tarFile:
        firstFilename = tarFile.getnames()[0]

        firstTarFile = tarFile.getmember(firstFilename)

        fileContents = tarFile.extractfile(firstTarFile).read().decode('utf-8')
        
        print(fileContents)

    return "Hello User: " + str(userID)