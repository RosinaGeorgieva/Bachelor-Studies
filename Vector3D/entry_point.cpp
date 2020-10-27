#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "vector_3d.h"
#include "vector_functions.h"

using namespace std;

const int kMaxNumberOfVectors = 10001;

int main()
{
	Vector3D array_of_vectors[kMaxNumberOfVectors];
	int number_of_vect;
	char sign;
	cin >> sign;///Позволяваме на потребителя да въведе един символ и според това какво е въведено,
	///разглеждаме няколко случая:
	if (IsPlus(sign))///символ +
	{
		cin >> number_of_vect;
		if (number_of_vect == 0) return 0;
		EnterArr(number_of_vect, array_of_vectors);
		cout << SumVectors(number_of_vect, array_of_vectors);
	}
	else if (IsMinus(sign))///символ -
	{
		cin >> number_of_vect;
		if (number_of_vect == 0) return 0;
		EnterArr(number_of_vect, array_of_vectors);
		cout << DiffVectors(number_of_vect, array_of_vectors);
	}
	else if (IsNeg(sign))///символ N
	{///След като знаем, че първият въведен символ е "N",
		char e, g;///няма друга възможност, освен да бъде въведена операцията "NEG",
		cin >> e >> g; ///затова позволяваме да бъдат въведени още 2 символа - буквите е и g
		cin >> number_of_vect;
		if (number_of_vect == 0) return 0;
		EnterArr(number_of_vect, array_of_vectors);
		Negative(number_of_vect, array_of_vectors);
	}
	else if (IsStar(sign))///символ *
	{
		float NUM;
		cin >> NUM;
		cin >> number_of_vect;
		if (number_of_vect == 0) return 0;
		EnterArr(number_of_vect, array_of_vectors);
		ProdNumAndVect(NUM, number_of_vect, array_of_vectors);

	}
	else///друг символ
	{   ///Тъй като приемаме, че входът е валиден,
		///ако не е в нито един от предишните случаи, то програмата ще влезе в този.
		char number[10]; ///Създаваме масив от char-ове, на който присвояваме 
		number[0] = sign;///на елемента с нулев индекс нашия въведен символ
		int i = 1;
		do
		{
			cin >> number[i];
			i++;
		} while (number[i - 1] != '*');///Въвеждаме останалите символи от числото,
		number[i-1] = '\0';///като добавяме терминиращата нула на мястото на '*', за да не се чете тя при парсването на масива към число float
		const char* kConstNumberString = number;///парсваме масива от символи към число с плаваща запетая
		float NUM;
		NUM = atof(kConstNumberString);
		cin >> number_of_vect;
		if (number_of_vect == 0) return 0;
		EnterArr(number_of_vect, array_of_vectors);
		ProdVectAndNum(NUM, number_of_vect, array_of_vectors);
	} 
	return 0;
}



