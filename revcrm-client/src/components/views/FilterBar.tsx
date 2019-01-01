
import * as React from "react"
import { Typography, Paper, Input, InputAdornment, Icon, Theme, createStyles, withStyles, WithStyles } from "@material-ui/core"
import { fade } from "@material-ui/core/styles/colorManipulator"
import { withMetadataContext, IMetadataContextProp } from "../meta/Metadata"
import { debounce } from "debounce"

export const FILTER_INTERVAL = 800

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    filterBar: {
        padding: 12,
        height: 60,
        display: "flex",
        alignItems: "center",
        color: "#fff",
        backgroundColor: theme.palette.primary.dark,
        zIndex: theme.zIndex.appBar - 1,
        position: "relative"
    },
    searchBox: {
        [theme.breakpoints.up("md")]: {
            minWidth: 350,
        },
    },
    searchInput: {
        color: "#fff",
        backgroundColor: fade(theme.palette.common.white, 0.15),
    },
    searchAdornment: {
        marginLeft: 4
    },
})

export interface IFilterBarProps extends
                    IMetadataContextProp,
                    WithStyles<typeof styles> {
    title: string
    entity: string
    onFilter(where: object): void
}

export interface IFilterBarState {
    searchText: string
}

export const FilterBar = withStyles(styles)(withMetadataContext(
    class extends React.Component<IFilterBarProps, IFilterBarState> {

    constructor(props: any) {
        super(props)
        this.state = {
            searchText: ""
        }
    }

    onSearchChanged = (e: React.ChangeEvent<HTMLInputElement>) => {
        this.setState({
            searchText: e.target.value
        })
        this.debouncedOnFilter()
    }

    debouncedOnFilter = debounce(() => {
        const searchVal = this.state.searchText
        if (searchVal.trim()) {
            this.props.onFilter({
                _text: { _search: searchVal }
            })
        }
        else {
            this.props.onFilter({})
        }
    }, FILTER_INTERVAL)

    render() {
        return (
            <div className={this.props.classes.root}>
                <Paper square className={this.props.classes.filterBar}>
                    <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                        {this.props.title}
                    </Typography>
                    <Input
                        className={this.props.classes.searchBox}
                        classes={{
                            root: this.props.classes.searchInput,
                        }}
                        disableUnderline={true}
                        placeholder="Search"
                        value={this.state.searchText}
                        onChange={this.onSearchChanged}
                        startAdornment={
                            <InputAdornment position="start" className={this.props.classes.searchAdornment}>
                                <Icon>search</Icon>
                            </InputAdornment>
                        }
                    />
                </Paper>
            </div>
        )
    }
}))
