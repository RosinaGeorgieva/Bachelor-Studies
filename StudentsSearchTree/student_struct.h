#ifndef STUDENT_STRUCT_H_
#define STUDENT_STRUCT_H_
#include <iostream>
#include <string>

using namespace std;

struct Student
{
	unsigned long long fac_num_;
	string first_name_;
	string last_name_;

	bool operator<(Student const &);
	bool operator>(Student const &);
	bool operator==(Student const&);
};

ostream & operator<<(ostream & os, const Student & s)
{
	os << s.fac_num_ << ' ' << s.first_name_ << ' ' << s.last_name_ << '\n';
	return os;
}

istream& operator>>(istream& is, Student& s)
{
	is >> s.fac_num_ >> s.first_name_ >> s.last_name_;
	return is;
}

bool Student::operator<(Student const & rhs)
{
	if (fac_num_ < rhs.fac_num_) return 1;
	return 0;
}

bool Student::operator>(Student const & rhs)
{
	if (fac_num_ > rhs.fac_num_) return 1;
	return 0;
}

bool Student::operator==(Student const& rhs)
{
	return fac_num_ == rhs.fac_num_;
}


#endif