
export type Omit<T, K extends string> = Pick<T, Exclude<keyof T, K>>;
