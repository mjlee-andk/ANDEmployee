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

    data class ResultDevisions(
        var code: Int = 404,
        var message: String = "",
        var data: List<TableDevisions>
    )

    data class ResultBoards(
        var code: Int = 404,
        var message: String = "",
        var data: List<TableBoards>
    )

    data class TableBoards(
        var id: String = "",
        var user_id: String ="",
        var user_name: String = "",
        var category_id: String = "",
        var category_name: String = "",
        var title: String = "",
        var contents: String = "",
        var image: String = "",
        var createdat: String = "",
        var comment_count: Int = 0,
        var like_clicked: Boolean = false,
        var updatedat: String = "",
        var click_count: Int = 0,
        var like_count: Int = 0,
        var comments: List<TableBoardComments>
    )

    data class TableBoardComments(
        var id: String = "",
        var user_id: String = "",
        var user_name: String = "",
        var comment: String = "",
        var createdat: String = "",
        var updatedat: String = ""
    )

    data class TableDevisions(
        var id: String = "",
        var name: String = "",
        var address: String = "",
        var telephone: String = "",
        var departments: List<TableDepartments>
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
            var division_name: String = "",
            var department_id: String = "",
            var department_name: String = "",
            var position_id: String = "",
            var position_name: String = ""
    )

    data class TableMemo(
        var id: String = "",
        var user_id: String = "",
        var employee_id: String = "",
        var memo: String = "",
        var createdat: String = "",
        var updatedat: String = ""
    )
}
