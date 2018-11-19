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

void sequentialSearch(fstream& file)
{
	size_t position = 0;
	bool fl = 0;
	file.open("StudentsGrades.db", ios::in);
	if (!file) {
		cerr << "File couldn't be opened! \n";
		return;
	}
	string fac_num;
	cin >> fac_num;
	string temp;// get line from file
	while (!file.eof())
	{
		getline(file, temp);
		position = temp.find(fac_num);// search for fac_num
		if (position != string::npos) // string::npos is returned if string is not found
		{
			cout << temp << endl;
			fl = 1;
			break;
		}
	}
	if (!fl)
	{
		cout << "Record not found! \n";
	}
	file.close();
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
