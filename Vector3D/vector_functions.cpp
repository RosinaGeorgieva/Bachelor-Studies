#include <iostream>
#include <cstring>
#include "vector_3d.h"
#include "vector_functions.h"

const int kMaxNumberOfVectors = 10001;

bool IsPlus(char c)
{
	return c == 43;
}

bool IsMinus(char c)
{
	return c == 45;
}

bool IsNeg(char c)
{
	return c == 'N';
}

bool IsStar(char c)
{
	return c == '*';
}


Vector3D SumVectors(int number_of_vect, Vector3D* array_of_vectors)
{
		Vector3D temp;
		for (int i = 0; i < number_of_vect; i++)
		{
			temp = temp + array_of_vectors[i];
		}
		return temp;
}

Vector3D DiffVectors(int number_of_vect, Vector3D* array_of_vectors)
{
	Vector3D temp;
	temp = array_of_vectors[0];
	for (int i = 1; i < number_of_vect; i++)
	{
		temp = temp - array_of_vectors[i];
	}
	return temp;
}

void EnterArr(int number_of_vect, Vector3D * array_of_vectors)
{
	for (int i = 0; i < number_of_vect; i++)
	{
		cin >> array_of_vectors[i];
	}
}

void EnterNumAndArr(int number_of_vect, Vector3D * array_of_vectors)
{
	cin >> number_of_vect;
	for (int i = 0; i < number_of_vect; i++)
	{
		cin >> array_of_vectors[i];
	}
}

void Negative(int number_of_vect, Vector3D* array_of_vectors) 

{
		for (int i = 0; i < number_of_vect - 1; i++)
		{
			 cout << -array_of_vectors[i] << " ";
		}
		cout << -array_of_vectors[number_of_vect - 1];
}

void ProdNumAndVect(float NUM, int number_of_vect, Vector3D* array_of_vectors)
{
	for (int i = 0; i < number_of_vect - 1; i++)
	{
		cout << NUM*array_of_vectors[i] << " ";
	}
	cout << NUM*array_of_vectors[number_of_vect - 1];
}

void ProdVectAndNum(float NUM, int number_of_vect, Vector3D* array_of_vectors) 
{
	for (int i = 0; i < number_of_vect - 1; i++)
	{
		cout << array_of_vectors[i]*NUM << " ";
	}
	cout << NUM*array_of_vectors[number_of_vect - 1];
}



