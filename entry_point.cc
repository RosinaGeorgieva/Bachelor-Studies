#include <iostream>
#include <fstream>
#include "functions.h"

using namespace std;

int main()
{
	fstream file;
	Operation test;
	while (test.GetOperation() != "exit")
	{
		cin >> test;
		test.DoOperation(file);
	}
	return 0;
}