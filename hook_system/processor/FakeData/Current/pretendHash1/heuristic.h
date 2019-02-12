#ifndef HEURISTIC_H
#define HEURISTIC_H

#include <vector>
#include <random>
#include <iostream>
#include <algorithm>


class Heuristic {
public:
    Heuristic();
    void GenerateSolution(int N, unsigned long seed = 0);
private:
    void PrintBoard(std::vector<int> board);
    std::vector<int> InitBoard(int N, std::mt19937 rng);
    int NumAttacks(std::vector<int> board, int r, int c);
    int GetRandomColumn(std::vector<int> board, int* safeQueens, std::mt19937 rng);
};

#endif //HEURISTIC_H