import * as React from "react"
import { IconButton, Icon, Menu, MenuItem, Avatar, Typography, Divider } from "@material-ui/core"
import { IAuthContextProp, withAuthContext } from "../auth/AuthContext"

export interface IUserMenuState {
    menuAnchorEl: any
}

export const UserMenu = withAuthContext(
    class extends React.Component<IAuthContextProp, IUserMenuState> {

    constructor(props: any) {
        super(props)
        this.state = {
            menuAnchorEl: null
        }
    }

    onButtonClick = (event: any) => {
        this.setState({ menuAnchorEl: event.currentTarget })
    }

    onMenuClose = () => {
        this.setState({ menuAnchorEl: null })
    }

    onLogout = async () => {
        this.props.auth.logout()
        this.onMenuClose()
    }

    render() {
        const user = this.props.auth.currentUser!
        return (
            <div>
                <IconButton
                    onClick={this.onButtonClick}
                    color="inherit"
                >
                    <Icon>account_circle</Icon>
                </IconButton>
                <Menu
                    anchorEl={this.state.menuAnchorEl}
                    open={Boolean(this.state.menuAnchorEl)}
                    onClose={this.onMenuClose}
                >
                    <div style={{
                        display: "flex", justifyContent: "space-around",
                        padding: 16, paddingBottom: 24, outline: "none"
                    }}>
                        <Avatar style={{ marginRight: 16 }}>R</Avatar>
                        <div>
                            <Typography variant="subtitle2" component="div">{user.fullName}</Typography>
                            <Typography variant="caption" component="div">{user.email}</Typography>
                        </div>
                    </div>
                    <Divider />
                    <MenuItem onClick={this.onMenuClose}>My Profile</MenuItem>
                    <MenuItem onClick={this.onLogout}>Logout</MenuItem>
                </Menu>
            </div>
        )
    }
})
