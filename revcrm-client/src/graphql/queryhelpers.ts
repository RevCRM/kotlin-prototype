import { DocumentNode, SelectionNode, FieldNode } from "graphql"

export interface IEntityQueryOptions {
    entity: string
    fields: string[]
}

/**
 * Returns a flat GraphQL query for the specified entity, e.g.:
 *   query {
 *     Account (where: { name: "bob" }) {
 *       results {
 *         field1,
 *         field2
 *       }
 *       meta {
 *         totalCount
 *       }
 *     }
 *   }
 */
export function getEntityQuery(options: IEntityQueryOptions): DocumentNode {
    const fieldSelections: SelectionNode[] = options.fields.map(field => {
        const fieldNode: FieldNode = {
            kind: "Field",
            name: {
                kind: "Name",
                value: field
            }
        }
        return fieldNode
    })
    const queryAST: DocumentNode = {
        kind: "Document",
        definitions: [
            {
                kind: "OperationDefinition",
                operation: "query",
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        {
                            kind: "Field",
                            name: {
                                kind: "Name",
                                value: options.entity
                            },
                            selectionSet: {
                                kind: "SelectionSet",
                                selections: [
                                    {
                                        kind: "Field",
                                        name: {
                                            kind: "Name",
                                            value: "results"
                                        },
                                        selectionSet: {
                                            kind: "SelectionSet",
                                            selections: fieldSelections
                                        }
                                    }
                                ]
                            }
                        }
                    ]
                }
            }
        ],
    }
    return queryAST
}
