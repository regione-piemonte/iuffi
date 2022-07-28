/* eslint-disable camelcase */

export interface IuffiBase<T> {
    _embedded: T;
    page_count?: number;
    page_size?: number;
    total_items: number;
    page?: number;
}
