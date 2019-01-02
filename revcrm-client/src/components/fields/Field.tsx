import * as React from "react"
import { IGridProps, GridItem } from "../views/Grid"
import { FormControl, InputLabel, Input, FormHelperText } from "@material-ui/core"
import { IMetadataContextProp, withMetadataContext } from "../meta/Metadata"
import { IFormContextProp, withFormContext } from "../views/FormView"

export interface IFieldProps extends
                    IGridProps,
                    IMetadataContextProp,
                    IFormContextProp {
    name: string
    label?: string
}

export const Field = withMetadataContext(withFormContext(
    class extends React.Component<IFieldProps> {

    render() {
        // move to constructor. dont assume entity or field is found
        const { meta, form } = this.props
        const entity = meta.getEntity(form.entity)!
        const field = entity.fields.find(f => f.name == this.props.name)

        if (!field) throw new Error(`Field '${this.props.name}' not found in data for entity '${entity.name}'`)

        const controlId = `field_${this.props.name}`
        const error = false
        const disabled = false
        const errorText = ""
        return (
            <GridItem {...this.props} >
                <FormControl fullWidth>
                    <InputLabel
                        htmlFor={controlId}
                        error={error}
                        disabled={disabled}
                    >
                        {this.props.label || field.label}
                    </InputLabel>
                    <Input
                        id={controlId}
                        value={form.entityData[field.name] || ""}
                        // onChange={(event) => props.onChange(event.target.value)}
                        error={error}
                        disabled={disabled}
                    />
                    {errorText &&
                        <FormHelperText error>
                            {errorText}
                        </FormHelperText>}
                </FormControl>
            </GridItem>
        )
    }
}))
