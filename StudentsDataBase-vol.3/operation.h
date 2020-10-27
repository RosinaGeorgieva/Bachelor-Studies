#ifndef OPERATION_H_
#define OPERATION_H_
#include <iostream>
#include <fstream>
#include "student_struct.h"
#include "util.h"

using namespace std;
class Operation
{
private:
	string operation_;
public:
	Operation(string = "buildIndex");

	bool DoOperation(fstream&,fstream&,fstream&);

	string GetOperation() const;

	friend istream& operator>>(istream&, Operation&);
};


istream & operator>>(istream & is, Operation & op)
{
	is >> op.operation_;
	return is;
}

Operation::Operation(string op)
{
	operation_ = op;
}

bool Operation::DoOperation(fstream& students_db,fstream& students_ids,fstream& students_num)
{
	ull s;
	if (operation_ == "sequentialSearch")
	{
		cin >> s;
		SequentialSearch(students_db, s);
		return 1;
	}
	else if (operation_ == "buildIndex")
	{
		BuildIndex(students_db,students_ids,students_num,1);
		return 1;
	}
	else if (operation_ == "search")
	{
		cin >> s;
		Search(students_db, students_ids, students_num,s);
		return 1;
	}
	else return 0;
}

string Operation::GetOperation() const
{
	return operation_;
}


#endif
