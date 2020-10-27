#include <iostream>

using namespace std;

class Vector3D;

Vector3D SumVectors(int number_of_vect, Vector3D* array_of_vectors);
Vector3D DiffVectors(int number_of_vect, Vector3D* array_of_vectors);
void EnterArr(int number_of_vect, Vector3D* array_of_vectors);
void Negative(int number_of_vect, Vector3D* array_of_vectors);
bool IsPlus(char sgn);
bool IsMinus(char sgn);
bool IsNeg(char sgn);
bool IsStar(char sgn);
void ProdNumAndVect(float NUM, int number_of_vect, Vector3D* array_of_vectors);
void ProdVectAndNum(float NUM, int number_of_vect, Vector3D* array_of_vectors);
