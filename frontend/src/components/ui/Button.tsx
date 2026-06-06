import type { ButtonHTMLAttributes } from "react";

const Button = ({ children, ...props }: ButtonHTMLAttributes<HTMLButtonElement>) => {
  return (
    <button
      {...props}
      className="rounded-lg bg-primary px-4 py-2 text-sm font-semibold text-on-primary transition hover:bg-primary-container"
    >
      {children}
    </button>
  );
};

export default Button;
