import { IFieldMetadata } from "../../meta/Metadata"
import { IFieldComponentProps } from "./props"
import { TextControl } from "./TextControl"
import { CheckboxControl } from "./CheckboxControl"

export function getFieldControlMapping(field: IFieldMetadata) {

    const mappings: {
        [fieldType: string]: React.ComponentType<IFieldComponentProps>
    } = {
        IntegerField: TextControl,
        FloatField: TextControl,
        DecimalField: TextControl,
        BooleanField: CheckboxControl,
        TextField: TextControl,
        DateField: TextControl,
        TimeField: TextControl,
        DateTimeField: TextControl,
        EnumField: TextControl,
        RelatedEntityField: TextControl,
    }

    return mappings[field.type]
}
