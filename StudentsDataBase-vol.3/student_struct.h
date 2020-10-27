#ifndef STUDENT_STRUCT_H_
#define STUDENT_STRUCT_H_
#include <iostream>
#include <string>
#include "index_struct.h"

using namespace std;

class Student
{
private:
	ull fac_num_;
	string first_name_;
	string last_name_;
	int grade_;

	void Copy(Student&);
public:
	Student(unsigned long long = 0, string = "", string = "", int = 2);
	Student(Student& const);
	Student& operator=(Student&);
	~Student();

	friend ostream& operator<<(ostream&, Student&);
	friend istream& operator>>(istream&, Student&);

	unsigned long long GetFacNum() const;
	string GetFirstName() const;
	string GetLastName() const;
	int GetGrade() const;

	void SetGrade(int);
};

ostream & operator<<(ostream & os, Student & s)
{
	os << s.GetFacNum() << ' ' << s.GetFirstName() << ' ' << s.GetLastName() << ' ' << s.GetGrade() << '\n';
	return os;
}

istream& operator>>(istream& is, Student& s)
{
	is >> s.fac_num_ >> s.first_name_ >> s.last_name_ >> s.grade_;
	return is;
}

inline void Student::Copy(Student & rhs)
{
	fac_num_ = rhs.fac_num_;
	first_name_ = rhs.first_name_;
	last_name_ = rhs.last_name_;
	grade_ = rhs.grade_;
}

Student::Student(unsigned long long fac_num, string first_name, string last_name, int grade)
{
	fac_num_ = fac_num;
	first_name_ = first_name;
	last_name_ = last_name;
	grade_ = grade;
}

inline Student::Student(Student & const rhs)
{
	Copy(rhs);
}

inline Student & Student::operator=(Student & const rhs)
{
	if (this != &rhs)
	{
		Copy(rhs);
	}
	return *this;
}

inline Student::~Student()
{
	fac_num_ = 0;
	first_name_ = "";
	last_name_ = "";
	grade_ = 2;
}

inline unsigned long long Student::GetFacNum() const
{
	return fac_num_;
}

inline string Student::GetFirstName() const
{
	return first_name_;
}

inline string Student::GetLastName() const
{
	return last_name_;
}

inline int Student::GetGrade() const
{
	return grade_;
}

inline void Student::SetGrade(int grade)
{
	grade_ = grade;
}

#endif
