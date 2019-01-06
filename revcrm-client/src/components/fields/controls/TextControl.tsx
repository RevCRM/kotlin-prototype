
import * as React from "react"

import Grid from "@material-ui/core/Grid"
import FormControl from "@material-ui/core/FormControl"
import FormHelperText from "@material-ui/core/FormHelperText"
import Input from "@material-ui/core/Input"
import InputLabel from "@material-ui/core/InputLabel"
import { IFieldComponentProps } from "./props"
import { getGridWidthProps } from "../../views/Grid"

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

    return (
        <Grid item {...gridWidthProps} style={props.style}>

            <FormControl fullWidth>
                <InputLabel
                    htmlFor={fieldId}
                    error={hasErrors}
                    shrink={true}
                >
                    {props.label}
                </InputLabel>
                <Input
                    id={fieldId}
                    type="text"
                    value={props.value || ""}
                    onChange={(event) => props.onChange(event.target.value)}
                    error={hasErrors}
                    disabled={props.disabled}
                    {...mlOptions}
                />
                {errorText &&
                    <FormHelperText error>
                        {errorText}
                    </FormHelperText>}
            </FormControl>

        </Grid>
    )
}
