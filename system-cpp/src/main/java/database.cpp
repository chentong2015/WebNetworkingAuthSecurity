#include <iostream>
#include <string>
#include <ctong/db/db_api.hpp>

using namespace ctong::db;

// Example using the DSL to abstract the SQL dialect
int main(int argc, char** argv) {
    connection cnx("sqlite");
    cnx.open(":memory:");
    statement stm(statement_builder(cnx).from("EMPLOYEES").select("NAME", "AGE", "SALARY").to_statement());

    for (result res(stm.execute()); !res.is_eof(); res.move_next() {
        std::cout
            << "- "          << res.get<std::string>("NAME")
            << ", age:"      << res.get<int>("AGE")
            << ", salary: $" << res.get<double>("SALARY")
            << '\n';
    }
    return 0;
}

// The DSL use natural expressions composition to build a filter:
statement stm(statement_builder(cnx).from("EMPLOYEES")
          .where(column("AGE") > var("max_age") && column("SALARY") < var("min_salary"))
          .select("NAME").to_statement());

stm.bind(var("max_age"), 50).bind(var("min_salary", 42000.0))
result res(stm.execute());
