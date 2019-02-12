#include "heuristic.h"

//
// Does a min-conflict search for a solution to the N-Queens problem
//
Heuristic::Heuristic(){

}

//
// Finds a solution to the N-Queens problem for an NxN board
//
void Heuristic::GenerateSolution(int N, unsigned long seed){
    int nConfigurations = 1;

    std::mt19937 rng;

    //if a seed was specified then use that seed, else get a random one
    if(seed == 0){
        std::random_device rd;
        seed = rd();
    }

    rng.seed(seed);

    //Initialize the board with least number of conflicts
    std::vector<int> board = InitBoard(N, rng);

    int iterations = 0;
    for(int nTries = 0; nTries < 1000; nTries++){
        int safeQueens = 0;

        //gets the column with the least possible conflicts randomly to minimize
        int col = GetRandomColumn(board, &safeQueens, rng);

        //if all the queens are safe then thats the solution
        if(safeQueens == N){
            PrintBoard(board);
            std::cout << "Seed Used: " << seed << std::endl; 
            std::cout << "Checked " << nConfigurations << " configurations for min-conflicts solution." << std::endl;

            return;
        }

        std::vector<int> possibleMoves;

        int leastAttacks = std::numeric_limits<int>::max();

        //find the rows with the least possible attacks
        for(int row = 0; row < N; row++){
            int attacks = NumAttacks(board, row, col);

            if(attacks < leastAttacks){
                leastAttacks = attacks;
                possibleMoves.clear();
                possibleMoves.push_back(row);
            }else if (attacks == leastAttacks){
                possibleMoves.push_back(row);
            }
        }

        //If there are possible moves then pick a random one
        if(!possibleMoves.empty()){
            nConfigurations++;
            std::uniform_int_distribution<std::mt19937::result_type> dist(0,possibleMoves.size()-1);


            int location = static_cast<int>(dist(rng));
            board[col] = possibleMoves[location];
        }

        //if we're stuck in a local minimum shuffle the board to try again
        iterations++;
        if(iterations >= N*3){
            iterations = 0;

            std::shuffle(std::begin(board), std::end(board), rng);
        }
    }

    //Couldn't find a solution in 1000 tries
    std::cout << "No solution found for N=" << N << std::endl;
    std::cout << "Seed Used: " << seed << std::endl;
    std::cout << "Checked " << nConfigurations << " configurations for solution." << std::endl;
}

//
// Initialize the board placing each queen in a column and row with least possible conflicts
//
std::vector<int> Heuristic::InitBoard(int N, std::mt19937 rng){
    std::vector<int> board;

    board.reserve(N);
    for(int y = 0; y < N; y++){
        board.push_back(0);
    }

    //pick the location for the first queen randomly
    std::uniform_int_distribution<std::mt19937::result_type> dist(0,N-1);
    int fRow = static_cast<int>(dist(rng));

    board[0] = fRow;

    //fill the rest of the columns with the spots of least attack
    for(int col = 1; col < N; col++){
        int lowestAttack = std::numeric_limits<int>::max();
        int lowestAttackLocation = -1;

        for(int row = 0; row < N; row++){
            int nAttacks = NumAttacks(board, row, col);

            if(nAttacks < lowestAttack){
                lowestAttack = nAttacks;
                lowestAttackLocation = row;
            }
        }

        board[col] = lowestAttackLocation;
    }

    return board;
}

//
// Gets the number of attacks possible on a position
//
int Heuristic::NumAttacks(std::vector<int> board,int r, int c){
    int numAttacks = 0;

    for(int col = 0; col < board.size(); col++){
        if(c != col){
            int row = board[col];

            //If the queen on another column is on the same row or diagonal
            // then add to the tally of attacks on the given position
            if(row == r || (std::abs(r - row) == std::abs(c - col))){
                numAttacks++;
            }
        }
    }

    return numAttacks;
}

//
// Prints the board to std output
//
void Heuristic::PrintBoard(std::vector<int> board){
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
// Gets a column that has the least possible conflicts on that columns queen
//
int Heuristic::GetRandomColumn(std::vector<int> board, int* safeQueens, std::mt19937 rng){
    std::vector<int> possibleColumns;
    int mostAttacks = 0;

    // Find the column(s) that have the least number of attack on it's queen 
    for(int col = 0; col < board.size(); col++){
        int nAttacks = NumAttacks(board, board[col], col);

        if(nAttacks == 0){
            (*safeQueens)++;
        }else {
            if(nAttacks == mostAttacks){
                possibleColumns.push_back(col);
            }else if(nAttacks > mostAttacks){
                possibleColumns.clear();
                possibleColumns.push_back(col);
                mostAttacks = nAttacks;
            }
        }
    }

    // If theres no columns with attacks on their queen return -1
    if(possibleColumns.empty()){
        return -1;
    }

    //else pick a random one from the list of possible queens
    std::uniform_int_distribution<std::mt19937::result_type> dist(0,possibleColumns.size()-1);

    int loc = static_cast<int>(dist(rng));

    return possibleColumns[loc];
}