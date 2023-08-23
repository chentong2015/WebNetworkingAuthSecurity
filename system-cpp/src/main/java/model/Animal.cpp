//
// Created by CHEN Tong on 03/01/2022.
//

#include "Animal.h"

Animal::Animal() {
    cout << "Animal creation without name" << endl;
}

Animal::Animal(string name) {
    str_name = name;
    cout << "Animal creation with name" << endl;
}

void Animal::eat() {
    cout << "Animal: " << str_name << " is eating" << endl;
}

void Animal::move() {
    cout << "Animal: " << str_name << " is moving" << endl;
}

Animal::~Animal() {
    cout << "~Animal" << endl;
}
