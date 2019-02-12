#ifndef BLIND_H
#define BLIND_H

#include <vector>
#include <iostream>

class Blind{
public:
    Blind();
    void GenerateSolution(int N);

private:
    bool IsPosSafe(std::vector<int> board, int r, int c);
    void PrintBoard(std::vector<int> board);
    std::vector<int> DFS(std::vector<int> board, int col = 0);

private:
    int queensPlaced;
    int totPosChecked;
};

#endif //BLIND_H