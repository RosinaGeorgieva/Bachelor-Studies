#ifndef STUDENT_H_
#define STUDENT_H_
#include <iostream>
#include <string>

using namespace std;

class Student
{
private:
	long long int fac_num_;
	string first_name_;
	string last_name_;
	int grade_;

	void Copy(Student&);
public:
	//The big four, without destructor; haven't used any dynamic memory allocation
	Student(long long int = 0, string = "", string = "", int = 2);
	Student(Student& const);
	Student& operator=(Student&);

	friend ostream& operator<<(ostream&, Student&);
	friend istream& operator>>(istream&, Student&);

	//Selectors that might be of use
	long long int GetFacNum() const;
	string GetFirstName() const;
	string GetLastName() const;
	int GetGrade() const;

};

#endif
