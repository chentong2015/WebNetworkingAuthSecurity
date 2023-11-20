//
// Created by CHEN Tong on 03/01/2022.
//

#ifndef JDK_JVM_MASTER_DOG_H
#define JDK_JVM_MASTER_DOG_H

#include "Animal.h"

class Dog : public Animal {

public:
    string sub_name;

public:
    Dog();
    Dog(string name, string sub_name);
    virtual ~Dog();
    virtual void move();
};

#endif //JDK_JVM_MASTER_DOG_H
