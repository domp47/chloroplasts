#include <iostream>
#include "blind.h"
#include "heuristic.h"

int main(int argc, char* argv[]){

    Blind b;
    Heuristic h;

    for(int i = 1; i <= 15; i++){
        std::cout << "N: " << i << std::endl;
        b.GenerateSolution(i);
        // std::cout << std::endl;
        h.GenerateSolution(i);

        // std::cout << std::endl << "Press Enter to Continue: ";
        // std::cin.get();

        std::cout << std::endl;
        std::cout << std::endl;
    }

    return 0;
}
