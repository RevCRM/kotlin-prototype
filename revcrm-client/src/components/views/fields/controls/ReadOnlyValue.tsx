
import * as React from "react"
import { Typography } from "@material-ui/core"

export const ReadOnlyValue: React.SFC = (props) => (
    <Typography style={{
        fontSize: 16, padding: "5px 0 7px", marginTop: 16,
        whiteSpace: "pre-line"
    }}>
        {props.children}
    </Typography>
)
