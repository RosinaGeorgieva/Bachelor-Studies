#ifndef FUNCTIONS_H_
#define FUNCTIONS_H_
#include <iostream>
#include <fstream>
#include "student.h"

using namespace std;

void create(fstream& file)
{
	file.open("StudentsGrades.db", ios::app);
	if (!file) {
		cerr << "File couldn't be opened! \n";
		return;
	}
	Student st;
	cin >> st;
	file << st.GetFacNum() << ' ' << st.GetFirstName() << ' ' << st.GetLastName() << ' ' << st.GetGrade() << ' ' << '\n';
	file.close();
	cout << "Record saved!" << endl;
	return;
}

bool sequentialSearch(fstream& file)
{
	unsigned long long fac_num;
	cin >> fac_num;
	file.open("StudentsGrades.db", ios::in);
	if (!file) { cerr << "The file couldn't be opened!\n"; return 0; }
	file.seekg(ios::beg);
	Student st;
	while (file >> st)
	{
		if (st.GetFacNum() == fac_num)
		{
			
				cout << st;
			file.close();
			return 1;
		}
	}
	cout << "Record not found! \n";
	file.close();
	return 0;
}

class Operation
{
private:
	string operation_;
public:
	Operation(string = "create");//setting default operation

	bool DoOperation(fstream&);

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

bool Operation::DoOperation(fstream& file)
{
	if (operation_ == "create")
	{
		create(file);
		return 1;
	}
	else if (operation_ == "sequentialSearch")
	{
		sequentialSearch(file);
		return 1;
	}
	else return 0;
}

string Operation::GetOperation() const
{
	return operation_;
}


#endif
