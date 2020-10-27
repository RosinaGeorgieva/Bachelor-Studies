#ifndef INDEX_STRUCT_H_
#define INDEX_STRUCT_H_
#include <iostream>
#include <fstream>

using namespace std;

typedef unsigned long long ull;

struct Index
{
	ull fac_num_;
	unsigned int offset_;

	bool operator<=(Index&);
	friend ostream& operator<<(ostream&, Index&);
	friend fstream& operator>>(fstream&, Index&);
};

ostream& operator<<(ostream& os, Index& i)
{
	os << i.fac_num_ << ' '<< i.offset_ << '\n';
	return os;
}

fstream& operator>>(fstream& is, Index& i)
{
	is >> i.fac_num_ >> i.offset_;
	return is;
}

bool Index::operator<=(Index& rhs)
{
	if (fac_num_ <= rhs.fac_num_) return 1;
	return 0;
}
#endif
