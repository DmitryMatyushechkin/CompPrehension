import * as io from 'io-ts'

/**
 * Merges type intersection to simple type
 * @example
 * type test = MergeIntersections<{ first: number } & { second: number }> 
 * // type test = { first: number, second: number }
 */
export type MergeIntersections<T> = 
 T extends object 
 ? { [K in keyof T]: T[K] }
 : T

/**
 * Recursive merge type intersection to simple type
 * @example
 * type test = MergeIntersections<{ first: number } & { second: number }> 
 * // type test = { first: number, second: number }
 */
export type MergeIntersectionsDeep<T> = 
  T extends object 
    ? { [K in keyof T]: MergeIntersectionsDeep<T[K]> }
    : T



export const TOptionalRequestResult = <T>(type: io.Type<T>, name?: string): io.Type<T | null | undefined | ''> =>
    io.union([type, io.null, io.undefined, io.literal('')], name ?? type.name);

