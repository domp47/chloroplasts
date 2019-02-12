import tarfile as tar

def submission(userID: str, email: str, data) -> str:
    
    # Discus whether we want to save it to a temp file and then send the path or just use the library thats built in to use memory

    with tar.open(fileobj=data.stream, mode='r') as tarFile:
        print(tarFile.getmembers())

    return "Hello User: " + str(userID)