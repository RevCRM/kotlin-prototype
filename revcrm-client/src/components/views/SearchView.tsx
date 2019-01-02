import * as React from "react"
import { Theme, createStyles, withStyles, WithStyles } from "@material-ui/core"
import { FilterBar } from "./FilterBar"
import { ListView } from "./ListView"
import { IMetadataContextProp, withMetadataContext } from "../meta/Metadata"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    resultsBox: {
        backgroundColor: "#fff"
    },
})

export interface ISearchViewProps extends
                    IMetadataContextProp,
                    WithStyles<typeof styles> {
    title: string
    entity: string
    showFields: string[]
    detailView?: string
}

export interface ISearchViewState {
    where: object
}

export const SearchView = withStyles(styles)(withMetadataContext(
    class extends React.Component<ISearchViewProps, ISearchViewState> {

    constructor(props: any) {
        super(props)
        this.state = {
            where: {}
        }
    }

    onFilter = (where: object) => {
        this.setState({ where })
    }

    render() {
        return (
            <div className={this.props.classes.root}>
                <FilterBar
                    title={this.props.title}
                    entity={this.props.entity}
                    onFilter={this.onFilter}
                />
                <div className={this.props.classes.resultsBox}>
                    <ListView
                        entity={this.props.entity}
                        fields={this.props.showFields}
                        where={this.state.where}
                        detailView={this.props.detailView}
                    />
                </div>
            </div>
        )
    }
}))
