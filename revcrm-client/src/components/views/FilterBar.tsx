
import * as React from "react"
import { Paper, Input, InputAdornment, Icon, Theme, createStyles, withStyles, WithStyles, Button } from "@material-ui/core"
import { fade } from "@material-ui/core/styles/colorManipulator"
import { withMetadataContext, IMetadataContextProp } from "../meta/Metadata"
import { debounce } from "debounce"
import { IViewManagerContextProp, withViewManagerContext } from "./ViewManager"

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
        flexGrow: 1
    },
    searchInput: {
        color: "#fff",
        backgroundColor: fade(theme.palette.common.white, 0.15),
    },
    searchAdornment: {
        marginLeft: 4
    },
    actionButton: {
        marginLeft: 12
    },
    actionButtonIcon: {
        marginRight: theme.spacing.unit
    }
})

export interface IFilterBarProps extends
    IMetadataContextProp,
    IViewManagerContextProp,
    WithStyles<typeof styles> {
    entity: string
    newRecordView?: string
    searchPlaceholderText?: string
    onFilter(where: object): void
}

export interface IFilterBarState {
    searchText: string
}

export const FilterBar = withStyles(styles)(withMetadataContext(withViewManagerContext(
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

        onNewButtonClicked = () => {
            if (this.props.newRecordView) {
                const [perspective, view] = this.props.newRecordView.split("/")
                this.props.view.changePerspective(perspective, view)
            }
        }

        render() {
            const { classes } = this.props
            const searchPlaceholderText = this.props.searchPlaceholderText || "Search"
            return (
                <div className={classes.root}>
                    <Paper square className={classes.filterBar}>
                        <Input
                            className={classes.searchBox}
                            classes={{
                                root: classes.searchInput,
                            }}
                            disableUnderline={true}
                            placeholder={searchPlaceholderText}
                            value={this.state.searchText}
                            onChange={this.onSearchChanged}
                            startAdornment={
                                <InputAdornment position="start" className={classes.searchAdornment}>
                                    <Icon>search</Icon>
                                </InputAdornment>
                            }
                        />
                        <div>
                            <Button variant="contained" color="secondary"
                                className={classes.actionButton}
                                onClick={this.onNewButtonClicked}
                            >
                                <Icon className={classes.actionButtonIcon}>add</Icon>
                                New
                            </Button>
                        </div>
                    </Paper>
                </div>
            )
        }
    })))
