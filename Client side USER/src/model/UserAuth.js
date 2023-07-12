export class UserAuth {

    constructor(username, password, role, newUsername = "", newPassword = ""){
        this.username = username;
        this.password = password;
        this.role = role;
        this.newUsername = newUsername;
        this.newPassword = newPassword;
    }
    
}