using Libraries.Db.Base;
using Microsoft.EntityFrameworkCore;

namespace Libraries.Db.Reposiitory.Interfaces
{
    public interface ICrudFactory
    {
        DbContext Context { get; }

        ICrudRepository<TEntity> Get<TEntity>() where TEntity : class, IDataType;
        ICrudRepository<TEntity> Get<TEntity>(DbContext context) where TEntity : class, IDataType;
    }
}
