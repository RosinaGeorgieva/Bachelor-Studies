#include "vector_3d.h"

Vector3D::Vector3D()
{
	x_ = 0;
	y_ = 0;
	z_ = 0;
}

Vector3D::Vector3D(double x, double y, double z)
{
	x_ = x;
	y_ = y;
	z_ = z;
}

Vector3D::~Vector3D()
{
}

double Vector3D::GetX() const
{
	return x_;
}

double Vector3D::GetY() const
{
	return y_;
}

double Vector3D::GetZ() const
{
	return z_;
}

Vector3D Vector3D::operator+(const Vector3D & secondVector) const
{
	return Vector3D(x_ + secondVector.x_, y_ + secondVector.y_, z_ + secondVector.z_);
}

Vector3D Vector3D::operator-(const Vector3D & secondVector) const
{
	return Vector3D(x_ - secondVector.x_, y_ - secondVector.y_, z_ - secondVector.z_);;
}

Vector3D Vector3D::operator-() const
{
	Vector3D temp = *this;
	if (x_ != 0) temp.x_ = -x_;
	if (y_ != 0) temp.y_ = -y_;
	if (z_ != 0) temp.z_ = -z_;
	return temp;
}

Vector3D Vector3D::operator*(double number) const
{
	return Vector3D(x_*number, y_*number, z_*number);
}

Vector3D operator*(double number, const Vector3D & vector)
{
	return Vector3D(number*vector.x_, number*vector.y_, number*vector.z_);
}

std::istream & operator>>(std::istream & iStream, Vector3D & vector)
{
	char openBr, closeBr, commaOne, commaTwo;
	iStream >> openBr >> vector.x_ >> commaOne >> vector.y_ >> commaTwo >> vector.z_ >> closeBr;
	return iStream;
}

std::ostream & operator<<(std::ostream & oStream, const Vector3D & vector)
{
	if (vector.x_ == -0)///Правим следната проверка, за да не връща -0 при умножение на отрицателна координата
	{///с 0:
		oStream << "(" << 0 << ",";
	}
	if (vector.x_ != -0)
	{
		oStream << "(" << vector.x_ << ",";
	}
	if (vector.y_ == -0)
	{
		oStream << 0 << ","; 
	}
	if (vector.y_ != -0)
	{
		oStream << vector.y_ << ",";
	}
	if (vector.z_ == -0)
	{
		oStream << 0 << ")";
	}
	 if (vector.z_ != -0)
	{
		oStream << vector.z_ << ")";
	}
	return oStream;
}
