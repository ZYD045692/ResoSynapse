import { md5 } from 'js-md5';

const stringToMD5 = (str: string): string => {
  return md5(str);
};

export default stringToMD5;