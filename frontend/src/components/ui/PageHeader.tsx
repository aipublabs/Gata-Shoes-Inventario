interface PageHeaderProps {
  title: string;
  description?: string;
  actionLabel?: string;
  actionIcon?: string;
  onAction?: () => void;
}

const PageHeader = ({
  title,
  description,
  actionLabel,
  actionIcon = "add",
  onAction,
}: PageHeaderProps) => {
  return (
    <div className="mb-10 flex flex-col md:flex-row md:items-end justify-between gap-4">
      <div>
        <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight mb-2">{title}</h1>
        {description ? <p className="text-slate-500">{description}</p> : null}
      </div>

      {actionLabel && onAction ? (
        <button
          type="button"
          onClick={onAction}
          className="flex items-center gap-2 bg-violet-600 text-white px-6 py-3 rounded-xl font-bold shadow-lg hover:bg-violet-700 transition-all"
        >
          <span className="material-symbols-outlined text-lg">{actionIcon}</span>
          <span>{actionLabel}</span>
        </button>
      ) : null}
    </div>
  );
};

export default PageHeader;
