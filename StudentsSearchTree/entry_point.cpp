#include <iostream>
#include "student_struct.h"
#include "operation.h"

using namespace std;

int main()
{
	BinaryOrderedTree t;
	Operation test;
	while (test.GetOperation() != "exit")
	{
		cin >> test;
		test.DoOperation(t);
	}
	return 0;
}