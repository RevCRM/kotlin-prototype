
import * as React from "react"
import MUIGrid from "@material-ui/core/Grid"

export type ColspanOptions = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12
export interface IGridProps {
    colspanNarrow?: ColspanOptions
    colspan?: ColspanOptions
    colspanWide?: ColspanOptions
}

export function getGridWidthProps(props: IGridProps) {
    return {
        xs: props.colspanNarrow || 12,
        md: props.colspan || 6,
        lg: props.colspanWide || props.colspan || 6
    }
}

export const Grid: React.SFC<{}> = (props) => {
    return (
        <MUIGrid container spacing={8}>
            {props.children}
        </MUIGrid>
    )
}

export const GridItem: React.SFC<IGridProps> = (props) => {
    const muiGridProps = getGridWidthProps(props)
    return (
        <MUIGrid item {...muiGridProps}>
            {props.children}
        </MUIGrid>
    )
}
