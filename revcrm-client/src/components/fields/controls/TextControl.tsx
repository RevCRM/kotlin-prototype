
import * as React from "react"

import { IFieldComponentProps } from "./props"
import { getGridWidthProps } from "../../views/Grid"
import { Grid, FormControl, InputLabel, Input, FormHelperText } from "@material-ui/core"
import { ReadOnlyValue } from "./ReadOnlyValue"

export const TextControl: React.StatelessComponent<IFieldComponentProps> = (props) => {

    const gridWidthProps = getGridWidthProps(props)
    const fieldId = props.field.name
    const { MultiLine } = props.field.properties

    const hasErrors = props.errors.length > 0
    let errorText = ""
    props.errors.forEach((err) => {
        errorText += err.message + ". "
    })

    const mlOptions: any = {}
    if (MultiLine == "true") {
        mlOptions.multiline = true
        mlOptions.rowsMax = 5
        mlOptions.rows = 5
    }

    const style = {
        minHeight: 64,
        ...props.style
    }

    return (
        <Grid item {...gridWidthProps} style={style}>

            <FormControl fullWidth>
                <InputLabel
                    htmlFor={fieldId}
                    error={hasErrors}
                    shrink={true}
                >
                    {props.label}
                </InputLabel>
                {!props.readonly &&
                    <Input
                        id={fieldId}
                        type="text"
                        value={props.value || ""}
                        onChange={(event) => props.onChange(event.target.value)}
                        error={hasErrors}
                        disabled={props.disabled}
                        {...mlOptions}
                    />}
                {props.readonly &&
                    <ReadOnlyValue>{props.value || ""}</ReadOnlyValue>}
                {errorText &&
                    <FormHelperText error>
                        {errorText}
                    </FormHelperText>}
            </FormControl>

        </Grid>
    )
}
