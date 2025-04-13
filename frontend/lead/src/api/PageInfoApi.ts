import axios from 'axios';
import {apiUrls}  from '@/api/api';

export async function fetchPage_Info() {
    try {
        const response = await axios.get(apiUrls.Get_Page_Info_API_URL);
        console.log('PageInfoApi',response.data)
        return response.data;
    } catch (err: unknown) {
        if (err instanceof Error) {
            console.error(`Data fetch error: ${err.message}`);
        } else {
            console.error(`Data fetch error: ${String(err)}`);
        }
    }
}
