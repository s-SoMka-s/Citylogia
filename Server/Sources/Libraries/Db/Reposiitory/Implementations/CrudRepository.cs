using Libraries.Db.Base;
using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace Libraries.Db.Reposiitory.Implementations
{
    public class CrudRepository<T> : ICrudRepository<T> where T: class, IDataType
    {
        protected DbContext Context { get; }
        protected DbSet<T> Set { get; }
        private readonly ICrudRepository<T> self;

        public CrudRepository(DbContext context)
        {
            Context = context;
            Set = context.Set<T>();

            self = this as ICrudRepository<T>;
        }

        async Task<T> ICrudRepository<T>.AddAsync(T entity)
        {
            var result = await self.AddAsync(new[] { entity });

            return result.First();
        }

        async Task<IEnumerable<T>> ICrudRepository<T>.AddAsync(IEnumerable<T> entities)
        {
            await Context.AddRangeAsync(entities);
            await Context.SaveChangesAsync();

            return entities;
        }

        Task<T> ICrudRepository<T>.FindAsync(long id)
        {
            var query = Set as IQueryable<T>;

            return query.SingleOrDefaultAsync(e => e.Id == id);
        }

        Task<T> ICrudRepository<T>.FindAsync(Expression<Func<T, bool>> predicate)
        {
            var query = Set as IQueryable<T>;

            return query.SingleOrDefaultAsync(predicate);
        }
    }
}
