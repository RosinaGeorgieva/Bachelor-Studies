#include <iostream>
#include "util.h"
#include "student_struct.h"
#include "operation.h"

using namespace std;

int main()
{
	fstream file;
	fstream ids_file;
	fstream num_stud_file;
	Operation test;
	while (test.GetOperation() != "exit")
	{
		cin >> test;
		test.DoOperation(file,ids_file,num_stud_file);
	}
	return 0;
}