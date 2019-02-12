from submission import Submission
from match import Match
from typing import List

class ProcessSubmissions():

    def __init__(self):
        self.submissionQueue = []

    def addToQueue(self, sub: Submission) -> bool:
        return True

    def processQueue(self):
        return

    def __sendEmail(self, emailAddr: str, msg: str) -> bool:
        return True
    
    def __generateResult(self, matches: List[Match]) -> str:
        return "<results></results>"