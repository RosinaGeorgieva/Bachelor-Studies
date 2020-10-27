#include <iostream>

using namespace std;

class Vector3D
{
private:
	double x_;
	double y_;
	double z_;
public:
	Vector3D();
	Vector3D(double x, double y, double z);
	~Vector3D();
	
	double GetX() const;
	double GetY() const;
	double GetZ() const;
	
	Vector3D operator+(const Vector3D & secondVector) const;
	Vector3D operator-(const Vector3D & secondVector) const;
	Vector3D operator-() const;
	Vector3D operator*(double number) const;
	
	friend Vector3D operator*(double number, const Vector3D & vector);
	friend std::istream & operator>>(std::istream & iStream, Vector3D & vector);
	friend std::ostream & operator<<(std::ostream & oStream, const Vector3D & vector);
};



