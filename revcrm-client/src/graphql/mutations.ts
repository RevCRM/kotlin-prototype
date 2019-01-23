import { DocumentNode, SelectionSetNode } from "graphql"
import { getFieldSelections, getSelectionSet } from "./helpers"
import { IEntityMetadata, IMetadataContext } from "../components/meta/Metadata"

export interface IEntityMutationOptions {
    meta: IMetadataContext
    entity: IEntityMetadata
    operation: "create" | "update" | "delete"
    resultFields: string[]
}

export interface IEntityMutationResult<T = any> {
    [mutationName: string]: {
        result: T | null
        validation: {
            hasErrors: boolean
            entityErrors: [
                {
                    entity: string
                    entityPath: string
                    code: string
                    message: string
                }
            ]
            fieldErrors: [
                {
                    entity: string
                    fieldPath: string
                    code: string
                    message: string
                }
            ]
        }
    }
}

/**
 * Returns a GraphQL mutation AST for the specified operation and entity, e.g.:
 *  mutation ($data: AccountInput!) {
 *      updateAccount (data: $data) {
 *          result {
 *              id
 *              name
 *              contact
 *              ...
 *          }
 *          validation {
 *              hasErrors
 *              ...
 *          }
 *      }
 *  }
 *
 * We dont use the gql tag here because the fields to be queried are metadata-driven
 */
export function getEntityMutation(options: IEntityMutationOptions): DocumentNode {
    const entityMutationName = getEntityMutationName(options)
    const entityInputType = options.entity + "Input"
    const selections = getFieldSelections(options.meta, options.entity, options.resultFields)
    const resultFieldSet = getSelectionSet(selections)
    const validationFieldSet = getValidationSelectionSet()

    return {
        kind: "Document",
        definitions: [
            {
                kind: "OperationDefinition",
                operation: "mutation",
                variableDefinitions: [
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "data" } },
                        type: {
                            kind: "NonNullType",
                            type: { kind: "NamedType", name: { kind: "Name", value: entityInputType } }
                        },
                    },
                ],
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        {
                            kind: "Field",
                            name: { kind: "Name", value: entityMutationName },
                            arguments: [
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "data" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "data" } },
                                },
                            ],
                            selectionSet: {
                                kind: "SelectionSet",
                                selections: [
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "result" },
                                        selectionSet: resultFieldSet
                                    },
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "validation" },
                                        selectionSet: validationFieldSet
                                    }
                                ]
                            }
                        }
                    ]
                }
            }
        ],
    }
}

export function getEntityMutationName(options: IEntityMutationOptions): string {
    return options.operation + options.entity
}

function getValidationSelectionSet(): SelectionSetNode {
    return {
        kind: "SelectionSet",
        selections: [
            {
                kind: "Field",
                name: { kind: "Name", value: "hasErrors" },
            },
            {
                kind: "Field",
                name: { kind: "Name", value: "entityErrors" },
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        { kind: "Field", name: { kind: "Name", value: "entity" } },
                        { kind: "Field", name: { kind: "Name", value: "entityPath" } },
                        { kind: "Field", name: { kind: "Name", value: "code" } },
                        { kind: "Field", name: { kind: "Name", value: "message" } },
                    ]
                }
            },
            {
                kind: "Field",
                name: { kind: "Name", value: "fieldErrors" },
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        { kind: "Field", name: { kind: "Name", value: "entity" } },
                        { kind: "Field", name: { kind: "Name", value: "fieldPath" } },
                        { kind: "Field", name: { kind: "Name", value: "code" } },
                        { kind: "Field", name: { kind: "Name", value: "message" } },
                    ]
                }
            }
        ]
    }
}
