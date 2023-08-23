#include <iostream>
#include <string>
#include <ctong/db/db_api.hpp>

using namespace ctong::db;

// Simple bootstrap example to perform a simple select on a SQLite in-memory database.
int main(int argc, char** argv) {
    connection cnx("sqlite");
    cnx.open(":memory:");
    statement stm("select * from EMPLOYEES", cnx);

    for (result res(stm.execute()); !res.is_eof(); res.move_next() {
        std::cout
            << "- "          << res.get<std::string>("NAME")
            << ", age:"      << res.get<int>("AGE")
            << ", salary: $" << res.get<double>("SALARY")
            << '\n';
    }
    return 0;
}
