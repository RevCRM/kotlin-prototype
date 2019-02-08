import * as React from "react"
import { IGridProps } from "../Grid"
import { IFieldMetadata, IMetadataContextProp, withMetadataContext } from "../../data/Metadata"
import { IFieldComponentProps, IFieldError, getStandardHTMLProps } from "./controls/props"
import { getFieldControlMapping } from "./controls/mappings"
import { IEntityContextProp, withEntityContext } from "../../data/EntityContext"

export interface IFieldProps extends
                    IGridProps,
                    IEntityContextProp,
                    IMetadataContextProp {
    name: string
    label?: string
    component?: React.ComponentType<IFieldComponentProps>
}

export interface IFieldState {
    value: any
}

export const Field = withEntityContext(withMetadataContext(
    class extends React.Component<IFieldProps, IFieldState> {
    field: IFieldMetadata

    constructor(props: any) {
        super(props)

        const { entity } = this.props
        this.field = entity.meta.fields.find(f => f.name == this.props.name)!
        if (!this.field)
            throw new Error(`Field '${this.props.name}' not found in data for entity '${entity.name}'`)

        this.state = {
            value: this.props.entity.data[this.field.name]
        }
    }

    static getDerivedStateFromProps(props: IFieldProps, state: IFieldState): IFieldState | null {
        if (state.value != props.entity.data[props.name]) {
            return {
                value: props.entity.data[props.name]
            }
        }
        return null
    }

    onChange = (value: any, trackValue = true) => {
        this.props.entity.onFieldChange(this.field, value)
        if (trackValue)
            this.setState({ value })
    }

    render() {

        const { value } = this.state
        const errors: IFieldError[] = []
        const disabled = false
        const readonly = this.props.entity.mode == "view"

        const componentProps: IFieldComponentProps = {
            meta: this.props.meta,
            entity: this.props.entity,
            field: this.field,
            label: this.props.label || this.field.label,
            colspanNarrow: this.props.colspanNarrow || 12,
            colspan: this.props.colspan || 6,
            colspanWide: this.props.colspanWide || this.props.colspan || 6,
            value,
            errors,
            disabled,
            readonly,
            onChange: this.onChange,
            children: this.props.children
        }
        const standardProps = getStandardHTMLProps(this.props)

        const Component = this.props.component || getFieldControlMapping(this.field)
        return <Component {...componentProps} {...standardProps} />
    }
}))
