#include "student.h"

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

Student::Student(long long int fac_num, string first_name, string last_name, int grade)
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

inline long long int Student::GetFacNum() const
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