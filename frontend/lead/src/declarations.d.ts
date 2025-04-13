import { AxiosInstance } from 'axios';
import Vue, { ComponentCustomProperties } from 'vue';

declare module 'vue' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}