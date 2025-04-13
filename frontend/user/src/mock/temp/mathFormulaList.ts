import type { mathFormulaType } from "@/api/model/mathFormulaModel";

export const MathFormulaTList: mathFormulaType[] = [
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><mn>1</mn><mo>&#x0002B;</mo><mn>2</mn><mo>&#x0002B;</mo><mn>3</mn><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mi>n</mi><mo>&#x0003D;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>2</mn></mrow></mfrac></mrow></math>',
        latex: '1 + 2 + 3 + \\cdots + n = \\frac{n(n+1)}{2}',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><mn>1</mn><mo>&#x0002B;</mo><mn>3</mn><mo>&#x0002B;</mo><mn>5</mn><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mo stretchy="false">&#x00028;</mo><mn>2</mn><mi>n</mi><mo>&#x02212;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo><mo>&#x0003D;</mo><msup><mi>n</mi><mn>2</mn></msup></mrow></math>',
        latex: '1 + 3 + 5 + \\cdots + (2n-1) = n^2',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><mn>2</mn><mo>&#x0002B;</mo><mn>4</mn><mo>&#x0002B;</mo><mn>6</mn><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mo stretchy="false">&#x00028;</mo><mn>2</mn><mi>n</mi><mo stretchy="false">&#x00029;</mo><mo>&#x0003D;</mo><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow></math>',
        latex: '2 + 4 + 6 + \\cdots + (2n) = n(n+1)',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><msup><mn>1</mn><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mn>2</mn><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mn>3</mn><mn>2</mn></msup><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><msup><mi>n</mi><mn>2</mn></msup><mo>&#x0003D;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo><mo stretchy="false">&#x00028;</mo><mn>2</mn><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>6</mn></mrow></mfrac></mrow></math>',
        latex: '1^2 + 2^2 + 3^2 + \\cdots + n^2 = \\frac{n(n+1)(2n+1)}{6}',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><msup><mn>1</mn><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mn>3</mn><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mn>5</mn><mn>2</mn></msup><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mo stretchy="false">&#x00028;</mo><mn>2</mn><mi>n</mi><mo>&#x02212;</mo><mn>1</mn><msup><mo stretchy="false">&#x00029;</mo><mn>2</mn></msup><mo>&#x0003D;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mn>4</mn><msup><mi>n</mi><mn>2</mn></msup><mo>&#x02212;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>3</mn></mrow></mfrac></mrow></math>',
        latex: '1^2 + 3^2 + 5^2 + \\cdots + (2n-1)^2 = \\frac{n(4n^2-1)}{3}',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><msup><mn>1</mn><mn>3</mn></msup><mo>&#x0002B;</mo><msup><mn>2</mn><mn>3</mn></msup><mo>&#x0002B;</mo><msup><mn>3</mn><mn>3</mn></msup><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><msup><mi>n</mi><mn>3</mn></msup><mo>&#x0003D;</mo><mfrac><mrow><msup><mi>n</mi><mn>2</mn></msup><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><msup><mo stretchy="false">&#x00029;</mo><mn>2</mn></msup></mrow><mrow><mn>4</mn></mrow></mfrac></mrow></math>',
        latex: '1^3 + 2^3 + 3^3 + \\cdots + n^3 = \\frac{n^2(n+1)^2}{4}',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><msup><mn>1</mn><mn>3</mn></msup><mo>&#x0002B;</mo><msup><mn>3</mn><mn>3</mn></msup><mo>&#x0002B;</mo><msup><mn>5</mn><mn>3</mn></msup><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mo stretchy="false">&#x00028;</mo><mn>2</mn><mi>n</mi><mo>&#x02212;</mo><mn>1</mn><msup><mo stretchy="false">&#x00029;</mo><mn>3</mn></msup><mo>&#x0003D;</mo><msup><mi>n</mi><mn>2</mn></msup><mo stretchy="false">&#x00028;</mo><mn>2</mn><msup><mi>n</mi><mn>2</mn></msup><mo>&#x02212;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow></math>',
        latex: '1^3 + 3^3 + 5^3 + \\cdots + (2n-1)^3 = n^2(2n^2-1)',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><mn>1</mn><mi>&#x000B7;</mi><mn>2</mn><mo>&#x0002B;</mo><mn>2</mn><mi>&#x000B7;</mi><mn>3</mn><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo><mo>&#x0003D;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x0002B;</mo><mn>2</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>3</mn></mrow></mfrac></mrow></math>',
        latex: '1 \cdot 2 + 2 \cdot 3 + \cdots + n(n+1) = \\frac{n(n+1)(n+2)}{3}',
    },
    {
        mathml: '<math xmlns="http://www.w3.org/1998/Math/MathML" display="inline"><mrow><mo stretchy="false">&#x00028;</mo><mi>a</mi><mo>&#x0002B;</mo><mi>b</mi><msup><mo stretchy="false">&#x00029;</mo><mi>n</mi></msup><mo>&#x0003D;</mo><msup><mi>a</mi><mi>n</mi></msup><mo>&#x0002B;</mo><mi>n</mi><msup><mi>a</mi><mrow><mi>n</mi><mo>&#x02212;</mo><mn>1</mn></mrow></msup><mi>b</mi><mo>&#x0002B;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x02212;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>2</mn><mo>&#x00021;</mo></mrow></mfrac><msup><mi>a</mi><mrow><mi>n</mi><mo>&#x02212;</mo><mn>2</mn></mrow></msup><msup><mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><mfrac><mrow><mi>n</mi><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x02212;</mo><mn>1</mn><mo stretchy="false">&#x00029;</mo><mo stretchy="false">&#x00028;</mo><mi>n</mi><mo>&#x02212;</mo><mn>2</mn><mo stretchy="false">&#x00029;</mo></mrow><mrow><mn>3</mn><mo>&#x00021;</mo></mrow></mfrac><msup><mi>a</mi><mrow><mi>n</mi><mo>&#x02212;</mo><mn>3</mn></mrow></msup><msup><mi>b</mi><mn>3</mn></msup><mo>&#x0002B;</mo><mo>&#x022EF;</mo><mo>&#x0002B;</mo><msup><mi>b</mi><mi>n</mi></msup></mrow></math>',
        latex: '(a+b)^n = a^n + na^{n-1}b + \\frac{n(n-1)}{2!}a^{n-2}b^2 + \\frac{n(n-1)(n-2)}{3!}a^{n-3}b^3 + \\cdots + b^n',
    },
    {
        mathml:"<math xmlns=\"http://www.w3.org/1998/Math/MathML\" display=\"inline\"><mrow><mo stretchy=\"false\">&#x00028;</mo><mi>a</mi><mo>&#x0002B;</mo><mi>b</mi><msup><mo stretchy=\"false\">&#x00029;</mo><mn>2</mn></msup><mo>&#x0003D;</mo><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo><mn>2</mn><mi>a</mi><mi>b</mi><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup></mrow></math>",
        latex:""
    },
];