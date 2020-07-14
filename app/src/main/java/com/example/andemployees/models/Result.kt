package com.example.andemployees.models

class Result {
    data class ResultUsers(
            var id: String = "",
            var account: String = "",
            var password: String = "",
            var is_valid: Int = 0
    )

    data class ResultBasic(
        var code: Int = 404,
        var message: String = ""
    )

    data class ResultChangePassword(
            var code: Int = 404,
            var message: String = ""
    )

    data class ResultLogin(
            var code: Int = 404,
            var message: String = "",
            var data: TableUser
    )

    data class ResultEmployees(
            var code: Int = 404,
            var message: String = "",
            var data: List<TableEmployees>
    )

    data class ResultEmployee(
        var code: Int = 404,
        var message: String = "",
        var data: TableEmployees
    )

    data class ResultMemo(
        var code: Int = 404,
        var message: String = "",
        var data: TableMemo
    )

    data class ResultDepartments(
        var code: Int = 404,
        var message: String = "",
        var data: List<TableDepartments>
    )

    data class TableDepartments(
        var id: String = "",
        var division_id: String = "",
        var name: String = "",
        var telephone: String = ""
    )

    data class TableUser(
            var id: String = "",
            var account: String = "",
            var password: String = "",
            var is_valid: Int = 1,
            var employee_id: String = ""
    )

    data class TableEmployees(
            var id: String = "",
            var name: String = "",
            var gender: Int = 1,
            var profile_img: String = "",
            var extension_number: String = "",
            var phone: String = "",
            var birth: String = "",
            var join_date: String = "",
            var leave_date: String = "",
            var division_id: String = "",
            var department_id: String = "",
            var position_id: String = ""
    )

    data class TableMemo(
        var id: String = "",
        var user_id: String = "",
        var employee_id: String = "",
        var memo: String = "",
        var date: String = ""
    )
}
