using Libraries.Db.Base;
using System;
using System.Collections.Generic;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace Libraries.Db.Reposiitory.Interfaces
{
    public interface ICrudRepository<T> where T : class, IDataType
    {
        Task<T> AddAsync(T entity);
        Task<IEnumerable<T>> AddAsync(IEnumerable<T> entities);

        Task<T> FindAsync(long id);
        Task<T> FindAsync(Expression<Func<T, bool>> predicate);
    }
}