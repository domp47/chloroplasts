#include "blind.h"

//
// Used for finding the solution to N-Queens using depth first search
// brute forcefully.
//
Blind::Blind(){

}

//
// Prints the board out to standard output, 
//
// Takes in a vector of ints representing the row the queen
//  is on in each column
//
void Blind::PrintBoard(std::vector<int> board){
    for(int y = 0; y < board.size(); y++){
        for(int x = 0; x < board.size(); x++){
            if(board[x] == y){
                std::cout << "Q ";
            }else{
                std::cout << "* ";
            }
        }
        std::cout << std::endl;
    }
}

//
// Finds a solution to the N-Queens problem with a NxN size board
//
void Blind::GenerateSolution(int N){
    std::vector<int> board;

    //set up the board
    board.reserve(N);
    for(int y = 0; y < N; y++){
        board.push_back(-1);
    }

    queensPlaced = 0;
    totPosChecked = 0;

    board = DFS(board);

    if(!board.empty()){
        PrintBoard(board);
    }else{
        std::cout << "No Solution Found for N=" << N << std::endl;
    }
    std::cout << "Checked " << totPosChecked << " configurations for DFS solution." << std::endl;
}

//
// Does a depth first search to try and find a solution
//
// Returns empty if there is no solution
//
std::vector<int> Blind::DFS(std::vector<int> board, int col){
    //if we placed all the queens then we have a solution
    if(queensPlaced == board.size()){
        return board;
    }
    //if not all the queens are placed and we've checked all the
    // columns and rows then there is no solution
    if(col >= board.size()){
        return std::vector<int>();
    }
    for(int y = 0; y < board.size(); y++){
        totPosChecked++;

        if(this->IsPosSafe(board, y, col)){
            int temp = board[col];
            board[col] = y;
            queensPlaced++;

            std::vector<int> res = DFS(board, col+1);
            if(!res.empty()){
                return res;
            }

            board[col] = temp;
            queensPlaced--;
        }
    }

    return std::vector<int>();
}

//
// Determines whether the queen is being attacked at the row and column provided.
//
// Returns true if the pos is not being attacked from anyone, otherwise returns false.
//
bool Blind::IsPosSafe(std::vector<int> board, int r, int c){
    for(int col = 0; col < board.size(); col++){
        if(c != col){
            int row = board[col];

            //If there's a piece thats on the same row or diagonal then the spot is not safe
            if(row != -1 && (row == r || (std::abs(r - row) == std::abs(c - col)))){
                return false;
            }
        }
    }
    return true;
}