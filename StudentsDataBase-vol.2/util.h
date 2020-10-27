#ifndef FUNCTIONS_H_
#define FUNCTIONS_H_
#include <iostream>
#include <fstream>
#include <string>
#include "student_struct.h"

using namespace std;

bool SequentialSearch(fstream& file, unsigned long long  fac_num, bool to_show)
{
	file.open("StudentsGrades.db", ios::in);
	if (!file) { cerr << "The file couldn't be opened!\n"; return 0; }
	file.seekg(ios::beg);
	Student st;
	while (file>>st)
	{
		if (st.GetFacNum() == fac_num)
		{
			if (to_show == 1)
			{
			cout << st; 
			}
			file.close(); 
			return 1;
		}
	}
	cout << "Record not found! \n";
	file.close();
	return 0;
}

bool Update(fstream & file, unsigned long long  to_update)
{
	if (SequentialSearch(file, to_update,0) == 0)
	{
		return 0;
	}
	file.open("StudentsGrades.db", ios::in|ios::out);
	if (!file) { cerr << "The file couldn't be opened! \n "; return 0; }
	file.clear();
	file.seekg(ios::beg);
	streampos position = file.tellg();
	int to_replace_grade;
	cin >> to_replace_grade;
	Student st;
	while (file >> st)
	{
		if (st.GetFacNum() == to_update)
		{
			file.seekp(position, ios::beg);
			st.SetGrade(to_replace_grade);
			file << st;
			cout << "Record saved! \n";
			file.close();
			return 1;
		}
		file.ignore();
		position = file.tellg();
	}
	
	file.close();
	return 0;
}

bool Delete(fstream & ifile, unsigned long long to_delete)
{
	if (SequentialSearch(ifile, to_delete,0) == 0)
	{
		return 0;
	}
	ifile.open("StudentsGrades.db", ios::in);
	ifile.clear();
	if (!ifile)
	{
		cerr << "The file couldn't be opened! \n";
		return 0;
	}
	ofstream ofile;
	ofile.open("StudentsGradesTemp.db", ios::out);
	if (!ofile)
	{
		cerr << "The file couldn't be opened! \n";
		return 0;
	}
	Student st;
	while (ifile >> st)
	{
		if (st.GetFacNum() != to_delete)
		{
			ofile << st;
		}
	}
	ifile.close();
	ofile.close();
	remove("StudentsGrades.db");
	rename("StudentsGradesTemp.db","StudentsGrades.db");
	cout << "Record deleted! \n";
	return 1;
}

class Operation
{
private:
	string operation_;
public:
	Operation(string = "sequentialSearch");//default operation is sequentialSearch

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
	unsigned long long  s;
	if (operation_ == "sequentialSearch")
	{
		cin >> s;
		SequentialSearch(file,s,1);
		return 1;
	}
	else if (operation_ == "update")
	{
		cin >> s;
		Update(file,s);
		return 1;
	}
	else if (operation_ == "delete")
	{
		cin >> s;
		Delete(file,s);
		return 1;
	}
	else return 0;
}

string Operation::GetOperation() const
{
	return operation_;
}


#endif
